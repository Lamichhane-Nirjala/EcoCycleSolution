package com.example.ecocyclesolution

import com.example.ecocyclesolution.repository.user.UserRepo
import com.example.ecocyclesolution.viewModel.UserViewModel
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class UserViewModelTest {

    @Mock
    lateinit var userRepo: UserRepo

    private lateinit var userViewModel: UserViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        userViewModel = UserViewModel(userRepo)
    }

    // ✅ Test 1: Login Repository Called
    @Test
    fun testLoginCallsRepository() {

        val callback: (Boolean, String) -> Unit = { _, _ -> }

        userViewModel.login(
            "test@gmail.com",
            "123456",
            callback
        )

        verify(userRepo).login(
            "test@gmail.com",
            "123456",
            callback
        )
    }

    // ✅ Test 2: Register Repository Called
    @Test
    fun testRegisterCallsRepository() {

        val callback: (Boolean, String, String) -> Unit =
            { _, _, _ -> }

        userViewModel.register(
            "test@gmail.com",
            "123456",
            callback
        )

        verify(userRepo).register(
            "test@gmail.com",
            "123456",
            callback
        )
    }

    // ✅ Test 3: Login Called Only Once
    @Test
    fun testLoginCalledOnce() {

        val callback: (Boolean, String) -> Unit = { _, _ -> }

        userViewModel.login("user@test.com", "pass", callback)

        verify(userRepo, times(1))
            .login("user@test.com", "pass", callback)
    }

    // ✅ Test 4: Register Never Called Without Trigger
    @Test
    fun testRegisterNotCalledInitially() {

        verify(userRepo, never())
            .register(anyString(), anyString(), any())
    }

    // ✅ Test 5: Multiple Login Attempts
    @Test
    fun testMultipleLoginCalls() {

        val callback: (Boolean, String) -> Unit = { _, _ -> }

        userViewModel.login("a@test.com", "111", callback)
        userViewModel.login("b@test.com", "222", callback)

        verify(userRepo, times(2))
            .login(anyString(), anyString(), any())
    }
}