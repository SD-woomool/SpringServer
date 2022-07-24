package app.joycourse.www.prod.entity.elasticsearch;


import app.joycourse.www.prod.entity.user.AgeRangeEnum;
import app.joycourse.www.prod.entity.user.GenderEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
//@Document(indexName = "user", createIndex = false)
public class UserDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String nickname;

    @Field(type = FieldType.Auto)
    private AgeRangeEnum ageRange;

    @Field(type = FieldType.Auto)
    private GenderEnum gender;
}
