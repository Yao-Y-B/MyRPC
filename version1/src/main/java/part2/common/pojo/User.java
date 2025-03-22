package part2.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
//自动生成一个Builder模式的视线，允许通过链式调用来构建
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    //客户和服务端共有
    private Integer id;
    private String userName;
    private boolean sex;
}
