package org.sf_ref.rdf.digest;

/**
 * A cryptographic digest
 *
 * @see Digestable
 * @see DigestUtil
 */

public interface Digest {

  public String MD5 = "MD5";
  public String SHA1 = "SHA-1";

  public String getDigestAlgorithm() throws DigestException;
  //  public int getDigestLength();
  public byte[] getDigestBytes() throws DigestException;
}

