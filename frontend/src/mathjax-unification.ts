import {MathjaxAdapter} from "./mathjax-adapter";

class MathjaxUnification extends MathjaxAdapter {
	connectedCallback() {
		super.connectedCallback();
		this.requestTypeset(null);
	}

	static get properties() {
		return {
			content: {type: String}
		}
	}

	protected showStep(_n: number): void {
		this.requestTypeset(null);
	}
}

customElements.define('tc-unification', MathjaxUnification);
