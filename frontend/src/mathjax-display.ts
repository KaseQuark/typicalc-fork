import {MathjaxAdapter} from "./mathjax-adapter";
import {TemplateResult} from "lit-html";

class MathjaxDisplay extends MathjaxAdapter {

    render(): TemplateResult {
        return super.render();
    }


    protected showStep(_n: number): void {
    }

    protected calculateSteps(): void {
    }
}

customElements.define('tc-display', MathjaxDisplay);