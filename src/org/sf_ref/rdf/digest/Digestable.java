package org.sf_ref.rdf.digest;

/**
 * An object that can produce a digest for itself implements this interface.
 *
 * @see Digestable
 * @see DigestUtil
 */

public interface Digestable {

  /**
   * @return a Digest
   */
  public Digest getDigest() throws DigestException;
}

