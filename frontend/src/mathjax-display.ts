import {MathjaxAdapter} from "./mathjax-adapter";

class MathjaxDisplay extends MathjaxAdapter {
    protected showStep(_n: number): void {}
}

customElements.define('tc-display', MathjaxDisplay);