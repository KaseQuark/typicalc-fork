// @ts-ignore
window.autoSelect = (className: string) => {
	let el = document.getElementsByClassName(className);
	Array.from(el).forEach(field => {
		field.addEventListener('focus', event => {
			const root = (event.target! as HTMLElement).shadowRoot!;
			let e: HTMLInputElement | HTMLTextAreaElement | null
				= root.querySelector<HTMLInputElement>('input');
			if (!e) {
				e = root.querySelector<HTMLTextAreaElement>('textArea')!;
			}
			e.setSelectionRange(0, e.value.length);
		});
	});
}
