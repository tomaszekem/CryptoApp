package db.entity;

import javax.persistence.*;

@Entity
@Table(name = "encrypted_data")
public class EncryptedData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "data")
    private String data;

    public EncryptedData(String data) {
        this.data = data;
    }
}
