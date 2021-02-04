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
            let semanticsMatch = (semantics: string) => semantics.indexOf("bspr_inference:") >= 0;
            // first, enumerate all of the steps
            let nodeIterator = document.createNodeIterator(this.shadowRoot, NodeFilter.SHOW_ELEMENT);
            let steps = [];
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
            // then fix some more mathjax layout issues
            for (const step of this.shadowRoot.querySelectorAll<HTMLElement>('g[typicalc="step"]')) {
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
                    // @ts-ignore
                    if (termAbove.parentNode.getAttribute("semantics") === "bspr_inferenceRule:down") {
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
            nodeIterator = document.createNodeIterator(this.shadowRoot, NodeFilter.SHOW_ELEMENT);
            steps = [];
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
                    const aboveStep1 = a.querySelector<HTMLElement>("#" + id + " g[typicalc=\"step\"]");
                    let above = [];
                    if (aboveStep1 != null) {
                        const parent = aboveStep1.parentNode!.parentNode! as HTMLElement;
                        parent.setAttribute("id", "typicalc-selector");
                        for (const node of parent.querySelectorAll("#typicalc-selector > g > g[typicalc=\"step\"")) {
                            above.push(node as HTMLElement);
                        }
                        parent.removeAttribute("id");
                    }
                    const rule = a.querySelector("#" + id + " g[semantics=\"bspr_inferenceRule:down\"]");
                    console.log(rule);
                    if (rule !== null) {
                        let i = 0;
                        for (const node of rule.childNodes) {
                            if (i !== 1) {
                                above.push(node);
                            }
                            i += 1;
                        }
                    }
                    const label = a.querySelector("#" + id +" g[semantics=\"bspr_prooflabel:left\"]");
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
            const svg = this.shadowRoot.querySelector<SVGElement>("svg")!;
            const nodeIterator2 = svg.querySelectorAll<SVGGraphicsElement>("g[data-mml-node='mtr']");
            let counter = 0;
            for (const a of nodeIterator2) {
                counter++;
                let left = null;
                let i = 0;
                for (const node of a.childNodes) {
                    if (i === 1 || i === 3) {
                        i += 1;
                        continue;
                    }
                    // @ts-ignore
                    const bbox = node.getBBox();
                    // @ts-ignore
                    const mat = node.transform.baseVal[0];
                    if (mat !== undefined) {
                        bbox.x += mat.matrix.e;
                    }
                    // 500 space between inference steps
                    // TODO: somehow broken since moving this algorithm to the TS file
                    if (left == null) {
                        left = bbox.x + bbox.width + 500;
                    } else {
                        mat.matrix.e -= bbox.x - left;
                        left = bbox.x + mat.matrix.e + bbox.width + 500;
                    }
                    i += 1;
                }
            }
            // @ts-ignore
            const bbox = svg.childNodes[1].getBBox();
            svg.setAttribute("viewBox", bbox.x + " " + bbox.y + " " + bbox.width + " " + bbox.height)
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
