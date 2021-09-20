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
		let stepIdx = 0;
		for (let text of this.shadowRoot!.querySelectorAll<HTMLElement>(".tc-text")) {
			let thisStepIdx = stepIdx;
			stepIdx++;
			text.onclick = () => {
				// @ts-ignore
				this.$server!.switchToStep(thisStepIdx);
			};
			// show initial step
			if (thisStepIdx === this.lastStepShown) {
				this.scrollToElementIfNeeded(text);
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
		this.lastStepShown = n;
		if (el) {
			this.scrollToElementIfNeeded(el);
			el.style.opacity = "1.0";
		}
	}

	private scrollToElementIfNeeded(el: HTMLElement) {
		const hostEl = this.shadowRoot!.host as HTMLElement;
		const dy = el.offsetTop - hostEl.offsetTop - hostEl.scrollTop;
		// end of text is below the container or the start of the text is above the container:
		if (dy + el.offsetHeight > hostEl.offsetHeight || dy < 0) {
			hostEl.scrollBy(0, -hostEl.scrollTop + el.offsetTop - hostEl.offsetTop);
		}
	}

	private getElementForStep(n: number): HTMLElement | null {
		return this.shadowRoot!.getElementById("tc-text-" + n)
	}
}

customElements.define('tc-explanation', MathjaxExplanation);
