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
            // the following algorithm fixes it by iterating over "rows" in the SVG created by MathJax
            // in each row, the elements are arranged to not overlap
            const svg = root.querySelector<SVGElement>("svg")!;
            const nodeIterator2 = [...svg.querySelectorAll<SVGGraphicsElement>("g[data-mml-node='mtr']")];
            // start layout fixes in the innermost part of the SVG
            nodeIterator2.reverse();
            const padding = 300;
            let counter = 0;
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
                        // this selector should be checked again when updating MathJax
                        const term = rule.childNodes[1].childNodes[0].childNodes[0].childNodes[1].childNodes[0] as SVGGraphicsElement;
                        // @ts-ignore
                        let w = -parentNode.getTransformToElement(term).e;
                        w += term.getBBox().width;
                        w += padding;
                        parentNode.setAttribute("x2", w.toString());
                    }
                    i += 1;
                }
            }
            // TODO: this does not scale the SVG correctly
            //const bbox = (svg.childNodes[1] as SVGGraphicsElement).getBBox();
            //svg.setAttribute("viewBox", bbox.x + " " + bbox.y + " " + bbox.width + " " + bbox.height);
            if (counter >= 3) {
                // should not be used on empty SVGs
                // @ts-ignore
                window.svgPanZoomFun(svg);
            }
            this.steps = steps;
            this.showStep(0);
            // @ts-ignore
            this.$server.setStepCount(steps.length);
        }
    }
}

customElements.define('tc-proof-tree', MathjaxProofTree);
