package authorizationserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue
    public Integer id;
    @Column(columnDefinition = "VARBINARY(MAX)")
    public Serializable doc;

    public Document(String doc) {
        this.doc = doc;
    }


}
