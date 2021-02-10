import {LitElement, html} from 'lit-element';
import {TemplateResult} from "lit-html";
declare let window: {
    MathJax: {
        typesetShadow: (shadowRoot: ShadowRoot, callback: () => void) => void,
        isInitialized: boolean,
    };
    addEventListener: (event: string, listener: () => void) => void;
};

export abstract class MathjaxAdapter extends LitElement {
    private execTypeset(shadowRoot: ShadowRoot) {
        if (window.MathJax) {
            window.MathJax.typesetShadow(shadowRoot, () => this.calculateSteps());
        }
    }

    protected requestTypeset() {
        this.updateComplete.then(() => {
            if (window.MathJax === undefined || !window.MathJax.isInitialized) {
                window.addEventListener('mathjax-initialized', () => this.execTypeset(this.shadowRoot!));
            } else {
                this.execTypeset(this.shadowRoot!);
            }
        });
    }

    connectedCallback() {
        super.connectedCallback();
        this.requestTypeset();
    }

    render(): TemplateResult {
        return html`<mjx-doc id="mjx-document"><mjx-head></mjx-head><mjx-body>
            <div id="tc-content"></div>
        </mjx-body></mjx-doc>`;
    }

    protected abstract showStep(n: number): void;

    protected calculateSteps(): void {
    }
}

