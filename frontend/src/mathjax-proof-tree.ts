import {MathjaxAdapter} from "./mathjax-adapter";
import {TemplateResult} from "lit-html";

class MathjaxProofTree extends MathjaxAdapter {
    private steps: any[] = [];

    render(): TemplateResult {
        return super.render();
    }


    protected showStep(n: number): void {
        for (let current = 0; current < this.steps.length; current++) {
            if (current <= n) {
                this.steps[current].style.display = "";
            } else {
                this.steps[current].style.display = "none";
            }
        }
    }

    protected calculateSteps(): void {
        if (this.shadowRoot !== null) {
            let nodeIterator = document.createNodeIterator(this.shadowRoot, NodeFilter.SHOW_ELEMENT);
            let steps = [];
            let a = null;
            while (a = nodeIterator.nextNode()) {
                // todo remove @ts suppress
                // @ts-ignore
                let semantics = a.getAttribute("semantics");
                if (semantics == null || a.nodeName !== "g") {
                    continue;
                }
                if (semantics.startsWith("bspr_inference:") || semantics.startsWith("bspr_axiom")) {
                    steps.push(a);
                    // @ts-ignore
                    a.style.display = "none";
                }
            }
            // @ts-ignore
            this.steps = steps;
            this.showStep(0);
            // @ts-ignore
            this.$server.setStepCount(steps.length);
        }
    }
}

customElements.define('tc-proof-tree', MathjaxProofTree);