import {MathjaxAdapter} from "./mathjax-adapter";

declare let window: {
    svgPanZoomFun: (svg: SVGElement, options: { fit: boolean }) => void;
}
// these attributes and functions are supported by major browsers, but TS does not know about them
declare global {
    interface SVGElement {
        viewBox: SVGAnimatedRect;
    }

    interface SVGGraphicsElement {
        getTransformToElement: (other: SVGGraphicsElement) => SVGMatrix;
    }

    interface SVGTransformList {
        [index: number]: SVGTransform;
    }
}

interface ProofTreeServer {
    setStepCount: (count: number) => void;
}

class MathjaxProofTree extends MathjaxAdapter {
    private steps: [SVGElement, SVGElement[]][] = [];
    private $server: ProofTreeServer | undefined;

    protected showStep(n: number): void {
        for (let current = 0; current < this.steps.length; current++) {
            this.steps[current][0].style.display = "none";
            for (const node of this.steps[current][1]) {
                node.style.display = "none";
            }
        }
        for (let current = 0; current < this.steps.length && current <= n; current++) {
            this.steps[current][0].style.display = "";
            for (const node of this.steps[current][1]) {
                node.style.display = "";
            }
        }
    }

    protected calculateSteps(): void {
        const semanticsMatch = (semantics: string) => semantics.indexOf("bspr_inference:") >= 0;
        const inferenceRuleSelector = 'g[semantics="bspr_inferenceRule:down"]';
        const labelSelector = 'g[semantics="bspr_prooflabel:left"]';
        const stepSelector = 'g[typicalc="step"]';
        // space between inference premises
        const padding = 300;

        if (this.shadowRoot !== null) {
            console.time('stepCalculation');
            const root = this.shadowRoot;
            // first, enumerate all of the steps
            let stepIdx = 0;
            for (const a of root.querySelectorAll<SVGElement>("g[semantics]")) {
                let semantics = a.getAttribute("semantics")!;
                if (semanticsMatch(semantics)) {
                    a.setAttribute("typicalc", "step");
                    a.setAttribute("id", "step" + stepIdx);
                    stepIdx++;
                }
            }
            // then create the steps
            let steps: [SVGElement, SVGElement[]][] = [];
            stepIdx = 0;
            for (const a of root.querySelectorAll<SVGElement>("g[semantics]")) {
                let semantics = a.getAttribute("semantics")!;
                if (semanticsMatch(semantics)) {
                    const id = "step" + stepIdx;
                    const idSelector = `#${id} `;
                    stepIdx++;

                    // find the next one/two steps above this one
                    const aboveStep1 = a.querySelector<SVGElement>(idSelector + stepSelector);
                    let above = [];
                    if (aboveStep1 != null) {
                        const parent = aboveStep1.parentElement!.parentElement!;
                        parent.setAttribute("id", "typicalc-selector");
                        for (const node of parent.querySelectorAll<SVGElement>('#typicalc-selector > g > ' + stepSelector)) {
                            above.push(node);
                        }
                        parent.removeAttribute("id");
                    }
                    const rule = a.querySelector(idSelector + inferenceRuleSelector);
                    if (rule !== null) {
                        let i = 0;
                        for (const node of rule.children) {
                            if (i !== 1) {
                                above.push(node as SVGElement);
                            }
                            i += 1;
                        }
                    }
                    const label = a.querySelector<SVGElement>(idSelector + labelSelector);
                    if (label) {
                        above.push(label);
                    }
                    if (stepIdx === 1) {
                        // initial step should not show premises
                        steps.push([a, []]);
                    }
                    steps.push([a, above]);
                }
            }
            // MathJax layout of bussproofs is sometimes wrong:
            // https://github.com/mathjax/MathJax/issues/2270
            // https://github.com/mathjax/MathJax/issues/2626
            // we fix it in two phases:
            // 1. fix overlapping by iterating over "rows" in the SVG created by MathJax
            // in each row, the elements are arranged to not overlap
            // 2. place inference conclusions under the center of their line
            const svg = root.querySelector<SVGElement>("svg")!;
            svg.viewBox.baseVal.width = Math.min(100000, svg.viewBox.baseVal.width);

            const nodeIterator = [...svg.querySelectorAll<SVGGraphicsElement>("g[data-mml-node='mtr']," + inferenceRuleSelector)];
            console.log(`working with ${nodeIterator.length} nodes`);
            // start layout fixes in the innermost part of the SVG
            nodeIterator.reverse();
            for (const row of nodeIterator) {
                const semantics = row.getAttribute("semantics");
                if (semantics === "bspr_inferenceRule:down") {
                    const conclusion = row.children[1].querySelector<SVGGraphicsElement>('g[data-mml-node="mstyle"]')!;
                    const conclusionBox = conclusion.getBBox();
                    const line = row.children[2] as SVGGraphicsElement;
                    const bbox = line.getBBox();

                    const offset2 = line.getTransformToElement(conclusion);
                    let dx = bbox.width / 2 + offset2.e - conclusionBox.width / 2;
                    dx += Number(line.getAttribute("x1"));
                    let table = row.parentNode as SVGGraphicsElement;
                    let prevNode;
                    let magicNumber = 0;
                    while (table.getAttribute("semantics") !== "bspr_inferenceRule:down") {
                        prevNode = table;
                        table = table.parentNode as SVGGraphicsElement;
                        if (table.getAttribute("data-mml-node") === "mtr" && table.childNodes.length === 3) {
                            for (let i = 0; i < table.childNodes.length; i++) {
                                if (table.childNodes[i] === prevNode) {
                                    magicNumber = i;
                                }
                            }
                        }
                        if (table === svg) {
                            break;
                        }
                    }
                    conclusion.transform.baseVal[0].matrix.e += dx;
                    if (magicNumber !== 0) {
                        continue;
                    }
                    if (table === svg) {
                        break; // last step
                    }
                    const lineBelow = table.children[2] as SVGGraphicsElement;
                    let lineStart = 0;
                    if (lineBelow) {
                        const offset1 = lineBelow.getTransformToElement(conclusion);
                        lineStart = -offset1.e;
                        const x1 = Number(lineBelow.getAttribute("x1"));
                        const x2 = Number(lineBelow.getAttribute("x2"));
                        lineBelow.setAttribute("x1", String(x1 + lineStart));
                        lineBelow.setAttribute("x2", String(x2 + lineStart));
                    }
                    const label = table.parentElement!.children[1] as SVGGraphicsElement;
                    if (label && label.transform) {
                        label.transform.baseVal[0].matrix.e += lineStart;
                    }
                } else {
                    let left = null;
                    let i = 0;
                    for (const rawNode of row.children) {
                        const node = rawNode as SVGGraphicsElement;
                        if (i === 1 || i === 3) {
                            i += 1;
                            continue; // spacing node
                        }
                        const bbox = node.getBBox();
                        const mat = node.transform.baseVal[0];
                        if (mat) {
                            bbox.x += mat.matrix.e;
                        }
                        // move box, and add padding between inference steps
                        if (left == null) {
                            // first box
                            left = bbox.x + bbox.width;
                        } else {
                            // this box has some elements left of it
                            // -> move to free space after other elements
                            mat.matrix.e -= bbox.x - left - padding;
                            left = bbox.x + mat.matrix.e + bbox.width;
                        }
                        if (i == 2) {
                            let parentNode = node.parentNode! as SVGGraphicsElement;
                            while (parentNode.getAttribute("semantics") !== "bspr_inferenceRule:down") {
                                parentNode = parentNode.parentNode! as SVGGraphicsElement;
                            }
                            parentNode = parentNode.children[2] as SVGGraphicsElement;
                            const rule = node.querySelector<SVGGraphicsElement>(inferenceRuleSelector);
                            if (rule) {
                                // this selector should be checked again when updating MathJax
                                const term = rule.children[1].children[0].children[0].children[1].children[0] as SVGGraphicsElement;
                                let w = -parentNode.getTransformToElement(term).e;
                                w += term.getBBox().width;
                                w += padding;
                                parentNode.setAttribute("x2", w.toString());
                            }
                        }
                        i += 1;
                    }
                }
            }
            const conclusion0 = svg.querySelector<SVGElement>(inferenceRuleSelector)!.children[1].children[0].children[0].children[1] as SVGGraphicsElement;
            const conclusionWidth = conclusion0.getBBox().width;
            const svgWidth = svg.viewBox.baseVal.width;
            const offset = (svg.children[1] as SVGGraphicsElement).getTransformToElement(conclusion0);
            (svg.children[1] as SVGGraphicsElement).transform.baseVal[0].matrix.e += offset.e + svgWidth / 2 - conclusionWidth / 2;
            console.timeEnd('stepCalculation');

            if (nodeIterator.length >= 3) {
                // should not be used on empty SVGs
                window.svgPanZoomFun(svg, { fit: false });
            }
            this.steps = steps;
            this.showStep(0);
            this.$server!.setStepCount(steps.length);
        }
    }
}

customElements.define('tc-proof-tree', MathjaxProofTree);
