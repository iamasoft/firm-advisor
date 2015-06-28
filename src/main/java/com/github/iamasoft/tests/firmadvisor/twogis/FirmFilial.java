package com.github.iamasoft.tests.firmadvisor.twogis;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * This class represents a single filial of a firm in 2GIS catalog.
 *
 * @author Kirill V. Karavaev
 */
@NotThreadSafe
@JsonIgnoreProperties(ignoreUnknown = true)
public class FirmFilial {

	/** Name of JSON attribute that contains a value for {@link #id}. */
	protected static final String PARAM_ID = "id";

	/** Name of JSON attribute that contains a value for {@link #hash}. */
	protected static final String PARAM_HASH = "hash";

	/** Name of JSON attribute that contains a value for {@link #name}. */
	protected static final String PARAM_FILIAL_NAME = "name";

	/** Name of JSON attribute that contains a value for {@link #city}. */
	protected static final String PARAM_CITY_NAME = "city_name";

	/** Name of JSON attribute that contains a value for {@link #address}. */
	protected static final String PARAM_FILIAL_ADDRESS = "address";

	/** Filial unique identifier. */
	@Nonnull
	private final String id;

	/** Filial unique hash, usable to retrieve filial profile. */
	@Nonnull
	private final String hash;

	/** Filial name. */
	@Nonnull
	private final String name;

	/** Name of a city this filial belongs to. */
	@Nonnull
	private final String city;

	/** Filial address. Optional. */
	@Nullable
	private String address;

	/**
	 * Constructs firm filial information holder.
	 * @param id
	 *        the filial unique identifier
	 * @param hash
	 *        the filial unique hash
	 * @param name
	 *        the filial name
	 * @param city
	 *        name of a city the filial belongs to
	 */
	@JsonCreator
	@ParametersAreNonnullByDefault
	public FirmFilial(@JsonProperty(PARAM_ID) String id, @JsonProperty(PARAM_HASH) String hash,
			@JsonProperty(PARAM_FILIAL_NAME) String name, @JsonProperty(PARAM_CITY_NAME) String city) {
		super();
		this.id = id;
		this.hash = hash;
		this.name = name;
		this.city = city;
	}

	/**
	 * @return the filial unique identifier
	 */
	@Nonnull
	public final String getId() {
		return id;
	}

	/**
	 * @return the filial unique hash, usable to retrieve filial profile
	 */
	@Nonnull
	public final String getHash() {
		return hash;
	}

	/**
	 * @return the filial name
	 */
	@Nonnull
	public final String getName() {
		return name;
	}

	/**
	 * @return name of a city the filial belongs to
	 */
	@Nonnull
	public final String getCity() {
		return city;
	}

	/**
	 * @return the filial address
	 */
	@CheckForNull
	public final String getAddress() {
		return address;
	}

	/**
	 * Sets the filial address.
	 * @param address
	 *        the address string to set
	 */
	@JsonSetter(PARAM_FILIAL_ADDRESS)
	public final void setAddress(@Nullable String address) {
		this.address = address;
	}

}
