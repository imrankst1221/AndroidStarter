package infixsoft.imrankst1221.android.starter.data

import infixsoft.imrankst1221.android.starter.data.model.User
import infixsoft.imrankst1221.android.starter.data.model.UserDetails
import infixsoft.imrankst1221.android.starter.data.model.UserNote

/**
 * @author imran.choudhury
 * 14/10/21
 */

object DummyDataSet {
    val DUMMY_USER1 = User(1, "mojombo", "https://avatars.githubusercontent.com/u/1?v=4", "")
    val DUMMY_USER2 = User(2, "defunkt", "https://avatars.githubusercontent.com/u/2?v=4", "")
    val DUMMY_USER3 = User(3, "defunkt", "https://avatars.githubusercontent.com/u/4?v=4", "")

    val DUMMY_USER1_DETAILS = UserDetails(DUMMY_USER1.userId, DUMMY_USER1.login,"Tom Preston-Werner", "https://avatars.githubusercontent.com/u/1?v=4",1, 1, "GitHub, Inc.", "")
    val DUMMY_USER2_DETAILS = UserDetails(DUMMY_USER2.userId, DUMMY_USER2.login,"Chris Wanstrath", "https://avatars.githubusercontent.com/u/2?v=4",1, 1, "GitHub, Inc.", "")
    val DUMMY_USER3_DETAILS = UserDetails(DUMMY_USER3.userId, DUMMY_USER3.login,"San Francisco", "https://avatars.githubusercontent.com/u/4?v=4",1, 1, "GitHub, Inc.", "")

    val DUMMY_USER1_NOTE = UserNote(DUMMY_USER1.userId, "User 1 note")
    val DUMMY_USER2_NOTE = UserNote(DUMMY_USER2.userId, "User 2 note")
    val DUMMY_USER3_NOTE = UserNote(DUMMY_USER3.userId, "User 3 note")
}