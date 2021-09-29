import {MathjaxAdapter} from "./mathjax-adapter";

// really basic class that only displays some static LaTeX code
class MathjaxDisplay extends MathjaxAdapter {
	connectedCallback() {
		super.connectedCallback();
		this.requestTypeset(null);
	}
}

customElements.define('tc-display', MathjaxDisplay);
