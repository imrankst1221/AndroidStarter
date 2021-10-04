package infixsoft.imrankst1221.android.starter.utilities

import java.io.IOException


class ApiException(message : String) : IOException(message)

class NoInternetException(message : String) : IOException(message)