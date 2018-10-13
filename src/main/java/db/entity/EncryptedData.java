package db.entity;

import org.apache.commons.io.FilenameUtils;

import javax.persistence.*;
import java.io.File;

@Entity
@Table(name = "encrypted_data")
public class EncryptedData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "file_extension", nullable = false)
    private String fileExtension;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

    public EncryptedData(byte[] encryptedBytes, File originalFile) {
        this.data = encryptedBytes;
        this.fileExtension = FilenameUtils.getExtension(originalFile.getName());
        this.fileName = FilenameUtils.getBaseName(originalFile.getName());
    }

    public EncryptedData() {
    }

    public byte[] getData() {
        return data;
    }

    public String getFileNameWithExtension() {
        return fileName + "." + fileExtension;
    }
}
