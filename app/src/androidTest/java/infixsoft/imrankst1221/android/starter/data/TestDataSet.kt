package infixsoft.imrankst1221.android.starter.data

import infixsoft.imrankst1221.android.starter.data.model.User
import infixsoft.imrankst1221.android.starter.data.model.UserDetails
import infixsoft.imrankst1221.android.starter.data.model.UserNote

/**
 * @author imran.choudhury
 * 14/10/21
 */

object TestDataSet {
    val user1 = User(1, "mojombo", "https://avatars.githubusercontent.com/u/1?v=4", "")
    val user2 = User(2, "defunkt", "https://avatars.githubusercontent.com/u/2?v=4", "")
    val user3 = User(3, "defunkt", "https://avatars.githubusercontent.com/u/4?v=4", "")

    val user1Details = UserDetails(user1.userId, "mojombo","Tom Preston-Werner", "https://avatars.githubusercontent.com/u/1?v=4",1, 1, "GitHub, Inc.", "")
    val user2Details = UserDetails(user2.userId, "defunkt","Chris Wanstrath", "https://avatars.githubusercontent.com/u/2?v=4",1, 1, "GitHub, Inc.", "")
    val user3Details = UserDetails(user3.userId, "defunkt","San Francisco", "https://avatars.githubusercontent.com/u/4?v=4",1, 1, "GitHub, Inc.", "")

    val user1Note = UserNote(user1.userId, "User 1 note")
    val user2Note = UserNote(user2.userId, "User 2 note")
    val user3Note = UserNote(user3.userId, "User 3 note")
}