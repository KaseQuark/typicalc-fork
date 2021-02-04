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
        for (let current = 0; current <= n; current++) {
            this.steps[current][0].style.display = "";
            for (const node of this.steps[current][1]) {
                node.style.display = "";
            }
        }
    }

    protected calculateSteps(): void {
        if (this.shadowRoot !== null) {
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
                if (semantics.startsWith("bspr_inference:") || semantics.startsWith("bspr_axiom")) {
                    a.setAttribute("typicalc", "step");
                    a.setAttribute("id", "step" + stepIdx);
                    stepIdx++;
                }
            }
            // then create the steps
            nodeIterator = document.createNodeIterator(this.shadowRoot, NodeFilter.SHOW_ELEMENT);
            steps = [];
            a = null;
            stepIdx = 0;
            while (a = nodeIterator.nextNode() as HTMLElement) {
                let semantics = a.getAttribute("semantics");
                if (semantics == null || a.nodeName !== "g") {
                    continue;
                }
                if (semantics.startsWith("bspr_inference:") || semantics.startsWith("bspr_axiom")) {
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
                        labelElement.style.display = "none";
                        above.push(labelElement);
                    }
                    if (stepIdx === 1) {
                        steps.push([a, []]);
                    }
                    if (!semantics.startsWith("bspr_axiom")) {
                        steps.push([a, above]);
                    }
                    a.style.display = "none";
                }
            }
            this.steps = steps;
            this.showStep(0);
            // @ts-ignore
            this.$server.setStepCount(steps.length);
        }
    }
}

customElements.define('tc-proof-tree', MathjaxProofTree);
