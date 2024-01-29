package com.yasunov.shiftapp2.network

import com.yasunov.shiftapp2.network.data.Results

interface NetworkDataSource {
    suspend fun getUsers(): Results
}