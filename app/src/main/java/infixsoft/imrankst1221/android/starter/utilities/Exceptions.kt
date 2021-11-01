package infixsoft.imrankst1221.android.starter.utilities

import java.io.IOException

/**
 * @author imran.choudhury
 * 31/10/21
 *
 * Android Exception helper
 */

class ApiException(message : String) : IOException(message)

class NoInternetException(message : String) : IOException(message)