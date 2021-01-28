import {MathjaxAdapter} from "./mathjax-adapter";
import {TemplateResult} from "lit-html";

class MathjaxProofTree extends MathjaxAdapter {

    render(): TemplateResult {
        return super.render();
    }


    protected showStep(_n: number): void {
    }

    protected calculateSteps(): void {
    }
}

customElements.define('tc-proof-tree', MathjaxProofTree);