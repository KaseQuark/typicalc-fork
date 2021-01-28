@NonNullFields
@NonNullApi
/**
 * The model package contains all classes needed to model the typed lambda calculus and the type inference algorithm.
 * To do so, it contains the sub-packages term, type, step and parser.
 * The model package itself contains the ModelInterface, functioning as an interface for the presenter
 * and the classes executing the type inference algorithm.
 * The class TypeInferer combines the three separated parts of the algorithm:
 * the building of the tree, execution of the unification and calculation of the most general unifier and final type.
 * An instance of TypeInferer, fitting the lambda term from the user input, is passed to the view to obtain the data
 * needed for the visualization of the computed steps of the algorithm.
 */
package edu.kit.typicalc.model;

import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;