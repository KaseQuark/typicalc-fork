import {MathjaxAdapter} from "./mathjax-adapter";

class MathjaxDisplay extends MathjaxAdapter {
    connectedCallback() {
        super.connectedCallback();
        this.requestTypeset();
    }
}

customElements.define('tc-display', MathjaxDisplay);