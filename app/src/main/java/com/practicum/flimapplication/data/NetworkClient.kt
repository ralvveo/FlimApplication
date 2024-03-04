package com.practicum.flimapplication.data

import com.practicum.flimapplication.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}