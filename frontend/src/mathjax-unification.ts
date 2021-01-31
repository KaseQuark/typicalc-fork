import {MathjaxAdapter} from "./mathjax-adapter";
import {TemplateResult} from "lit-html";

class MathjaxUnification extends MathjaxAdapter {

    static get properties() {
        return {
            content: {type: String}
        }
    }

    render(): TemplateResult {
        return super.render();
    }

    protected showStep(_n: number): void {
        this.requestTypeset();
    }
}

customElements.define('tc-unification', MathjaxUnification);