export const hoverAreaAroundElements: string = `
.typicalc-type, g[semantics='bspr_prooflabel:left'] {
	/* cross-browser-compatibility: Chrome does not support the stroke trick, but instead bounding-box (which is not supported by Firefox..) */
	stroke: transparent; stroke-width: 600px; pointer-events: all; pointer-events: bounding-box;
}`;