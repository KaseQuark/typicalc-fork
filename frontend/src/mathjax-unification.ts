import {MathjaxAdapter} from "./mathjax-adapter";

class MathjaxUnification extends MathjaxAdapter {

    static get properties() {
        return {
            content: {type: String}
        }
    }

    protected showStep(_n: number): void {
        this.requestTypeset();
    }
}

customElements.define('tc-unification', MathjaxUnification);