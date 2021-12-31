@GenericGenerator(
        name = "ID_GENERATOR",
        strategy = "enhanced-sequence"
        // ,
        // parameters = {
        //         @Parameter(
        //                 name = "dialect_id_seq",
        //                 value = "dialect_id_seq"
        //         ),
        //         @Parameter(
        //                 name = "gender_id_seq",
        //                 value = "gender_id_seq"
        //         ),
        //         @Parameter(
        //                 name = "language_id_seq",
        //                 value = "language_id_seq"
        //         ),
        //         @Parameter(
        //                 name = "mediafile_id_seq",
        //                 value = "mediafile_id_seq"
        //         ),
        //         @Parameter(
        //                 name = "part_of_speech_id_seq",
        //                 value = "part_of_speech_id_seq"
        //         ),
        //         @Parameter(
        //                 name = "relation_type_id_seq",
        //                 value = "relation_type_id_seq"
        //         ),
        //         @Parameter(
        //                 name = "initial_value",
        //                 value = "1"
        //         )
        // }
)
package org.lekitech.gafalag.entity;

import org.hibernate.annotations.GenericGenerator;
