const subscripted = [...'\u2080\u2081\u2082\u2083\u2084\u2085\u2086\u2087\u2088\u2089'];

// @ts-ignore
window.addTypeInputListener = (className: string) => {
	const el = document.getElementsByClassName(className);
	if (el) {
		const listener = (e: Event) => {
			const area = (e.target! as HTMLElement).shadowRoot!.querySelector<HTMLInputElement>('input');
			modifyValue(area!);
		};
		Array.from(el).forEach(function (element) {
			element.addEventListener('keyup', listener);
			element.addEventListener('focus', listener);
		});
	}
}

function modifyValue(area: HTMLInputElement) {
	const value = parseBack(area.value);
	const start = area.selectionStart;
	const end = area.selectionEnd;
	// ignore brackets, allow '>' or spaces in front and '-' or spaces at the end of string
	area.value = value.replace(/(^|\s+|\(|\)|>)t[0-9]+(?=\s+|\)|\(|-|$)/ig, replacer);
	area.selectionStart = start;
	area.selectionEnd = end;
}

function replacer(value: string) {
	value = value.replace('t', '\u03C4');
	return value.replace(/[0123456789]/g, toUnicode);
}

function toUnicode(number: string, ..._args: any[]) {
	return subscripted[Number(number)];
}

function toNumber(unicode: string, ..._args: any[]) {
	const result = subscripted.indexOf(unicode);
	if (result > -1) {
		return result.toString();
	} else {
		return unicode;
	}
}

function parseBack(value: string) {
	// @ts-ignore
	value = value.replaceAll('\u03C4', 't');
	return value.replace(/./g, toNumber);
}
