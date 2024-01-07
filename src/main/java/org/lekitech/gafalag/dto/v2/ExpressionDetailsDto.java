package org.lekitech.gafalag.dto.v2;

import java.util.List;
import java.util.Optional;

/**
 * It represents the whole definition of a word for the following cases:
 * <ul>
 *      <li> Definition per dictionary (e.g. definition from <b>Babakhanov</b> dictionary will have one ExpressionDetails object and definition from Hajiyev dictionary will have another ExpressionDetails object). </li>
 *      <li> Same expression occurs multiple times in the same dictionary (e.g. <b>Babakhanov</b> dictionary has 2 definitions for the word "АВАТIА", each of them will have its own ExpressionDetails object). </li>
 *      <li> Expressions with Roman numerals, so definitions under each numeral have their own ExpressionDetails object. </li>
 * </ul>
 *
 * @param gr                grammatical forms of the expression.
 * @param inflection
 * @param definitionDetails {@link DefinitionDetailsDto}
 * @param examples          {@link ExampleDto}
 */
public record ExpressionDetailsDto(
        String gr,
        String inflection,
        List<DefinitionDetailsDto> definitionDetails,
        Optional<List<ExampleDto>> examples
) {
}
