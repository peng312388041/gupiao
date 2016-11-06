package com.gupiao.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "share")
public class Share implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3898315283519672230L;

	@Id // ��������Ϊ����
	@GeneratedValue(strategy = GenerationType.AUTO) // ���ݲ�ͬ���ݿ��Զ�ѡ����ʵ�id���ɷ�����
	private Integer id;

	private String code;
	private String name;
	private int inited;
	private String lastupdate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getInited() {
		return inited;
	}

	public void setInited(int inited) {
		this.inited = inited;
	}

	public String getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}

	@Override
	public String toString() {
		return "Share [id=" + id + ", code=" + code + ", name=" + name + ", inited=" + inited + ", lastupdate="
				+ lastupdate + "]";
	}

}
