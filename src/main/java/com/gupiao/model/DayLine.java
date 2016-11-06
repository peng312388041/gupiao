package com.gupiao.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dayline")
public class DayLine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3898315283519672230L;
	@Id // 声明此列为主键
	@GeneratedValue(strategy = GenerationType.AUTO) // 根据不同数据库自动选择合适的id生成方案，
	private Integer id;
	private String code;
	private String name;
	// 收盘价
	private double spj;
	// 涨跌额
	private double zde;
	// 涨跌幅
	private double zdf;
	// 开盘价
	private double kpj;
	// 最高价
	private double maxPrice;
	// 最低价
	private double minPrice;
	// 成交量
	private long cjl;
	// 成交额
	private long cje;
	// 日期
	private String date;

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

	public double getSpj() {
		return spj;
	}

	public void setSpj(double spj) {
		this.spj = spj;
	}

	public double getZde() {
		return zde;
	}

	public void setZde(double zde) {
		this.zde = zde;
	}

	public double getZdf() {
		return zdf;
	}

	public void setZdf(double zdf) {
		this.zdf = zdf;
	}

	public double getKpj() {
		return kpj;
	}

	public void setKpj(double kpj) {
		this.kpj = kpj;
	}

	public double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}

	public long getCjl() {
		return cjl;
	}

	public void setCjl(long cjl) {
		this.cjl = cjl;
	}

	public long getCje() {
		return cje;
	}

	public void setCje(long cje) {
		this.cje = cje;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "DayLine [id=" + id + ", code=" + code + ", name=" + name + ", spj=" + spj + ", zde=" + zde + ", zdf="
				+ zdf + ", kpj=" + kpj + ", maxPrice=" + maxPrice + ", minPrice=" + minPrice + ", cjl=" + cjl + ", cje="
				+ cje + ", date=" + date + "]";
	}
}
