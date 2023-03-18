package com.gym.record.photo;

import com.gym.record.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "record_photo")
public class RecordPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String imgUrl;
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    public RecordPhoto(String imgUrl, Record record, String fileName) {
        this.imgUrl = imgUrl;
        this.record = record;
        this.fileName= fileName;
    }

    //==객체 생성 메서드==//
    public void createRecord(Record record){
        this.record = record;
    }

}
