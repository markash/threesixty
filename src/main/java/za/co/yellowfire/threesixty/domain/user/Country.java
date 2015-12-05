package za.co.yellowfire.threesixty.domain.user;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

public class Country implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int number;
	@Id
	private String commonName;
	private String formalName;
	private String type;
	private String subType;
	private String sovereignty;
	private String capital;
	private String currencyCode;
	private String currencyName;
	private String telephoneCode;
	private String iso316612LetterCode;
	private String iso316613LetterCode;
	private String countryCode;
	private String tldCode;
	
	public Country(int number, String commonName, String formalName, String type, String subType, String sovereignty,
			String capital, String currencyCode, String currencyName, String telephoneCode, String iso316612LetterCode,
			String iso316613LetterCode, String countryCode, String tldCode) {
		super();
		this.number = number;
		this.commonName = commonName;
		this.formalName = formalName;
		this.type = type;
		this.subType = subType;
		this.sovereignty = sovereignty;
		this.capital = capital;
		this.currencyCode = currencyCode;
		this.currencyName = currencyName;
		this.telephoneCode = telephoneCode;
		this.iso316612LetterCode = iso316612LetterCode;
		this.iso316613LetterCode = iso316613LetterCode;
		this.countryCode = countryCode;
		this.tldCode = tldCode;
	}

	public int geNumber() { return this.number; }
	
	public String getCommonName() {
		return commonName;
	}

	public String getFormalName() {
		return formalName;
	}

	public String getType() {
		return type;
	}

	public String getSubType() {
		return subType;
	}

	public String getSovereignty() {
		return sovereignty;
	}

	public String getCapital() {
		return capital;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public String getTelephoneCode() {
		return telephoneCode;
	}

	public String getIso316612LetterCode() {
		return iso316612LetterCode;
	}

	public String getIso316613LetterCode() {
		return iso316613LetterCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getTldCode() {
		return tldCode;
	}

	@Override
	public String toString() {
		return commonName == null ? "null" : commonName + (formalName != null ? " (" + formalName + ")" : "");
	}
}
