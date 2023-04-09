package com.kantian.track.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "snowflakeId")
    @GenericGenerator(
            name = "snowflakeId",
            strategy = "com.kantian.track.jpa.SnowflakeIdentifierGenerator")
    private Long id;

    /**
     * 创建时间
     */
    @CreatedDate
    private Date createDate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    private Date updateDate;

    @Version
    private Integer version;
}
