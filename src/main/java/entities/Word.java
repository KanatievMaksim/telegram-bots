package entities;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Word {
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String word;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private String[] examples;

    @Getter
    @Setter
    private String[] translation;


}
