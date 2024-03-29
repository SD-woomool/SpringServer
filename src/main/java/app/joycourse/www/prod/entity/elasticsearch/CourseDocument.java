package app.joycourse.www.prod.entity.elasticsearch;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
//@Document(indexName = "course")
public class CourseDocument {
    @Id
    private String id;

    @Field(type = FieldType.Nested)
    private UserDocument user;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Text)
    private String title;
}
