package app.joycourse.www.prod.mapper;

import app.joycourse.www.prod.dto.PlaceInfoDto;
import app.joycourse.www.prod.entity.Place;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapStruct {
    MapStruct INSTANCE = Mappers.getMapper(MapStruct.class);

    @Mapping(target = "courseDetails", expression = "java(null)")
    Place placeDtoToEntity(PlaceInfoDto placeInfoDto);
}
