import {MathjaxAdapter} from "./mathjax-adapter";

class MathjaxExplanation extends MathjaxAdapter {
	private lastStepShown: number = 0

	connectedCallback() {
		super.connectedCallback();
		this.requestTypeset(null);
	}

	static get properties() {
		return {
			content: {type: String}
		}
	}

	protected calculateSteps(_extraData: any) {
		let first = true;
		let stepIdx = 0;
		for (let text of this.shadowRoot!.querySelectorAll<SVGGraphicsElement>(".tc-text")) {
			let thisStepIdx = stepIdx;
			stepIdx++;
			text.onclick = () => {
				// @ts-ignore
				this.$server!.switchToStep(thisStepIdx);
			};
			if (first) {
				first = false; // show first step
				continue;
			}
			text.style.opacity = "0.5";
		}
	}

	protected showStep(n: number): void {
		let lastEl = this.getElementForStep(this.lastStepShown);
		if (lastEl) {
			lastEl.style.opacity = "0.5";
		}
		let el = this.getElementForStep(n);
		if (el) {
			this.lastStepShown = n;
			// scroll to element if needed
			const hostEl = this.shadowRoot!.host as HTMLElement;
			const dy = el.offsetTop - hostEl.offsetTop - hostEl.scrollTop;
			if (dy > hostEl.offsetHeight || dy < 0) {
				hostEl.scrollBy(0, -hostEl.scrollTop + el.offsetTop - hostEl.offsetTop);
			}
			el.style.opacity = "1.0";
		}
	}

	private getElementForStep(n: number): HTMLElement | null {
		return this.shadowRoot!.getElementById("tc-text-" + n)
	}
}

customElements.define('tc-explanation', MathjaxExplanation);
