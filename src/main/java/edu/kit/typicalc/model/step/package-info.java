@NonNullFields
@NonNullApi
/**
 * The step package models the inference steps that are executed while generating the prof tree.
 * To represent the different kinds of typing rules that can be applied, InferenceStep has various subclasses.
 * These subclasses can be produced by factories, appropriate to the desired context.
 */
package edu.kit.typicalc.model.step;

import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;