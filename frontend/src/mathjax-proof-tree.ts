import {MathjaxAdapter} from "./mathjax-adapter";
import {TemplateResult} from "lit-html";

class MathjaxProofTree extends MathjaxAdapter {
    private steps: any[] = [];

    render(): TemplateResult {
        return super.render();
    }


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
        if (this.shadowRoot !== null) {
            console.time('stepCalculation');
            const root = this.shadowRoot;
            const semanticsMatch = (semantics: string) => semantics.indexOf("bspr_inference:") >= 0;
            // first, enumerate all of the steps
            let nodeIterator = document.createNodeIterator(root, NodeFilter.SHOW_ELEMENT);
            let a = null;
            let stepIdx = 0;
            while (a = nodeIterator.nextNode() as HTMLElement) {
                let semantics = a.getAttribute("semantics");
                if (semantics == null || a.nodeName !== "g") {
                    continue;
                }
                if (semanticsMatch(semantics)) {
                    a.setAttribute("typicalc", "step");
                    a.setAttribute("id", "step" + stepIdx);
                    stepIdx++;
                }
            }
            // then fix some mathjax layout issues
            for (const step of root.querySelectorAll<HTMLElement>('g[typicalc="step"]')) {
                const infRule = step.querySelector<HTMLElement>('g[semantics="bspr_inferenceRule:down"]');
                if (infRule === null) {
                    continue;
                }
                if (infRule.childNodes.length != 3) {
                    continue;
                }
                const stepAbove = infRule.childNodes[0] as HTMLElement & SVGGraphicsElement;
                const infRuleAbove = stepAbove.querySelector<HTMLElement>('g[semantics="bspr_inferenceRule:down"]')!;
                if (infRuleAbove === null || infRuleAbove.childNodes.length != 3) {
                    continue;
                }
                let termAbove = infRuleAbove.childNodes[1] as SVGGraphicsElement;
                let dx = termAbove.getBBox().x;
                termAbove = termAbove.parentElement as HTMLElement & SVGGraphicsElement;
                while (true) {
                    if ((termAbove.parentNode! as HTMLElement).getAttribute("semantics") === "bspr_inferenceRule:down") {
                        break;
                    }
                    if (termAbove.transform.baseVal !== null) {
                        if (termAbove.transform.baseVal.numberOfItems !== 1) {
                            termAbove = termAbove.parentElement as HTMLElement & SVGGraphicsElement;
                            continue;
                        }
                        // @ts-ignore
                        dx += termAbove.transform.baseVal[0].matrix.e;
                        termAbove = termAbove.parentElement as HTMLElement & SVGGraphicsElement;
                    }
                }
                // @ts-ignore
                stepAbove.transform.baseVal[0].matrix.e -= dx;
            }
            // then create the steps
            nodeIterator = document.createNodeIterator(root, NodeFilter.SHOW_ELEMENT);
            let steps = [];
            stepIdx = 0;
            while (a = nodeIterator.nextNode() as HTMLElement) {
                let semantics = a.getAttribute("semantics");
                if (semantics == null || a.nodeName !== "g") {
                    continue;
                }
                if (semanticsMatch(semantics)) {
                    const id = "step" + stepIdx;
                    stepIdx++;

                    // find the next one/two steps above this one
                    const aboveStep1 = a.querySelector<HTMLElement>("#" + id + ' g[typicalc="step"]');
                    let above = [];
                    if (aboveStep1 != null) {
                        const parent = aboveStep1.parentNode!.parentNode! as HTMLElement;
                        parent.setAttribute("id", "typicalc-selector");
                        for (const node of parent.querySelectorAll('#typicalc-selector > g > g[typicalc="step"]')) {
                            above.push(node as HTMLElement);
                        }
                        parent.removeAttribute("id");
                    }
                    const rule = a.querySelector("#" + id + ' g[semantics="bspr_inferenceRule:down"]');
                    if (rule !== null) {
                        let i = 0;
                        for (const node of rule.childNodes) {
                            if (i !== 1) {
                                above.push(node);
                            }
                            i += 1;
                        }
                    }
                    const label = a.querySelector("#" + id + ' g[semantics="bspr_prooflabel:left"]');
                    if (label !== null) {
                        const labelElement = label as HTMLElement;
                        above.push(labelElement);
                    }
                    if (stepIdx === 1) {
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
            // @ts-ignore
            svg.viewBox.baseVal.width = Math.min(100000, svg.viewBox.baseVal.width);
            let counter = 0;
            let oldWidth = NaN;
            let newWidth = (svg.childNodes[1] as SVGGraphicsElement).getBBox().width;
            // rendering LaTeX often requires multiple passes over the input..
            let iterations = 0;
            let extraIterations = 1;
            while (isNaN(oldWidth) || Math.abs(oldWidth - newWidth) > 5000 || extraIterations > 0) {
                if (!(isNaN(oldWidth) || Math.abs(oldWidth - newWidth) > 5000)) {
                    extraIterations--;
                }
                iterations++;
                oldWidth = newWidth;
                const nodeIterator2 = [...svg.querySelectorAll<SVGGraphicsElement>("g[data-mml-node='mtr']")];
                // start layout fixes in the innermost part of the SVG
                nodeIterator2.reverse();
                const padding = 300;
                counter = 0;
                for (const row of nodeIterator2) {
                    counter++;
                    let left = null;
                    let i = 0;
                    for (const rawNode of row.childNodes) {
                        const node = rawNode as SVGGraphicsElement;
                        if (i === 1 || i === 3) {
                            i += 1;
                            continue;
                        }
                        const bbox = node.getBBox();
                        // @ts-ignore
                        const mat = node.transform.baseVal[0];
                        if (mat !== undefined) {
                            bbox.x += mat.matrix.e;
                        }
                        // move box, and add padding between inference steps
                        if (left == null) {
                            left = bbox.x + bbox.width;
                        } else {
                            mat.matrix.e -= bbox.x - left - padding;
                            left = bbox.x + mat.matrix.e + bbox.width;
                        }
                        if (i == 2) {
                            let parentNode = node.parentNode as SVGGraphicsElement;
                            while (parentNode.getAttribute("semantics") !== "bspr_inferenceRule:down") {
                                parentNode = parentNode.parentNode as SVGGraphicsElement;
                            }
                            parentNode = parentNode.childNodes[2] as SVGGraphicsElement;
                            const rule = node.querySelector<SVGGraphicsElement>('g [semantics="bspr_inferenceRule:down"]')!;
                            if (rule !== null) {
                                // this selector should be checked again when updating MathJax
                                const term = rule.childNodes[1].childNodes[0].childNodes[0].childNodes[1].childNodes[0] as SVGGraphicsElement;
                                // @ts-ignore
                                let w = -parentNode.getTransformToElement(term).e;
                                w += term.getBBox().width;
                                w += padding;
                                parentNode.setAttribute("x2", w.toString());
                            }
                        }
                        i += 1;
                    }
                }
                const nodeIterator1 = [...svg.querySelectorAll<SVGGraphicsElement>('g[semantics="bspr_inferenceRule:down"]')];
                // start layout fixes in the innermost part of the SVG
                nodeIterator1.reverse();
                for (const rule of nodeIterator1) {
                    const conclusion = (rule.childNodes[1] as HTMLElement).querySelector<SVGGraphicsElement>('g[data-mml-node="mstyle"]')!;
                    const conclusionBox = conclusion.getBBox();
                    const line = rule.childNodes[2] as SVGGraphicsElement;
                    const bbox = line.getBBox();
                    // @ts-ignore
                    const offset2 = line.getTransformToElement(conclusion);
                    let dx = bbox.width / 2 + offset2.e - conclusionBox.width / 2;
                    dx += Number(line.getAttribute("x1"));
                    // @ts-ignore
                    let table = rule.parentNode as SVGGraphicsElement;
                    while (table.getAttribute("semantics") !== "bspr_inferenceRule:down") {
                        table = table.parentNode as SVGGraphicsElement;
                        if (table.tagName.toLowerCase() === "svg") {
                            break;
                        }
                    }
                    // @ts-ignore
                    conclusion.transform.baseVal[0].matrix.e += dx;
                    if (table.tagName.toLowerCase() === "svg") {
                        break; // last step
                    }
                    const lineBelow = table.childNodes[2] as SVGGraphicsElement;
                    if (lineBelow) {
                        const x = Number(lineBelow.getAttribute("x1"));
                        const x2 = Number(lineBelow.getAttribute("x2"));
                        lineBelow.setAttribute("x1", String(x + dx));
                        lineBelow.setAttribute("x2", String(x2 + dx));
                    }
                    // @ts-ignore
                    const label = table.parentNode.childNodes[1] as SVGGraphicsElement;
                    if (label && label.transform) {
                        // @ts-ignore
                        label.transform.baseVal[0].matrix.e += dx;
                    }
                }
                newWidth = (svg.childNodes[1] as SVGGraphicsElement).getBBox().width;
            }
            // @ts-ignore
            const conclusion0 = svg.querySelector('g[semantics="bspr_inferenceRule:down"]').childNodes[1].childNodes[0].childNodes[0].childNodes[1] as SVGGraphicsElement;
            const conclusionWidth = conclusion0.getBBox().width;
            // @ts-ignore
            const svgWidth = svg.viewBox.baseVal.width;
            // @ts-ignore
            const offset = (svg.childNodes[1] as SVGGraphicsElement).getTransformToElement(conclusion0);
            // @ts-ignore
            (svg.childNodes[1] as SVGGraphicsElement).transform.baseVal[0].matrix.e += offset.e + svgWidth / 2 - conclusionWidth / 2;
            console.timeEnd('stepCalculation');
            console.log("Iterations: " + iterations);

            if (counter >= 3) {
                // should not be used on empty SVGs
                // @ts-ignore
                window.svgPanZoomFun(svg, { fit: false });
            }
            this.steps = steps;
            this.showStep(0);
            // @ts-ignore
            this.$server.setStepCount(steps.length);
        }
    }
}

customElements.define('tc-proof-tree', MathjaxProofTree);
