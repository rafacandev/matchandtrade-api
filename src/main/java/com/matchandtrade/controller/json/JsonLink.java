package com.matchandtrade.controller.json;

/**
 * Stores link attributes for hypermedia usage.
 * 
 * @author rafael.santos.bra
 */
public class JsonLink {

	private String rel;
	private String href;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JsonLink other = (JsonLink) obj;
		if (href == null) {
			if (other.href != null)
				return false;
		} else if (!href.equals(other.href))
			return false;
		if (rel == null) {
			if (other.rel != null)
				return false;
		} else if (!rel.equals(other.rel))
			return false;
		return true;
	}

	public String getHref() {
		return href;
	}

	public String getRel() {
		return rel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((href == null) ? 0 : href.hashCode());
		result = prime * result + ((rel == null) ? 0 : rel.hashCode());
		return result;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

}
