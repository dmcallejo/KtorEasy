package com.batcuevasoft.api.user

import com.batcuevasoft.database.dao.Users
import com.batcuevasoft.model.PostUserBody
import com.batcuevasoft.model.PutUserBody
import com.batcuevasoft.model.User
import com.batcuevasoft.util.PasswordEncryption
import org.koin.core.KoinComponent
import org.koin.core.inject

object UserApiImpl : UserApi, KoinComponent {

    private val usersDao by inject<Users>()
    private val passwordEncryption by inject<PasswordEncryption>()

    override fun getUserById(id: Int): User? {
        return usersDao.getUserById(id)
    }

    override fun createUser(postUser: PostUserBody): User? {
        val encryptedUser = postUser.copy(password = passwordEncryption.encryptPassword(postUser.password))
        val key: Int? = (usersDao.insertUser(encryptedUser))[usersDao.id]
        return key?.let {
            usersDao.getUserById(it)
        }
    }

    override fun removeUser(userId: Int): Boolean {
        return usersDao.deleteUser(userId)
    }

    override fun updateUserProfile(userId: Int, putUserBody: PutUserBody): User? {
        return usersDao.updateUser(userId, putUserBody)
    }

    override fun getUserByUsername(username: String): User? {
        return usersDao.getUserByName(username)
    }
}