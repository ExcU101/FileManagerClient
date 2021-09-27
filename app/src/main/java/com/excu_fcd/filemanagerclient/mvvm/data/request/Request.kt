package com.excu_fcd.filemanagerclient.mvvm.data.request

import com.excu_fcd.filemanagerclient.mvvm.data.Idable
import com.excu_fcd.filemanagerclient.mvvm.data.Nameable
import com.excu_fcd.filemanagerclient.mvvm.utils.RequestBuilderMarker
import com.excu_fcd.filemanagerclient.mvvm.utils.item
import com.excu_fcd.filemanagerclient.mvvm.utils.request
import kotlin.random.Random

class Request<I> private constructor(
    private val name: String,
    private val id: Int,
    private val operations: List<Operation<I>>,
) : Nameable, Idable {

    private var progress = 0
    private var status: Status = Status.empty()

    override fun getId(): Int {
        return id
    }

    internal fun updateProgress(value: Int) {
        if (operations.size == value + progress) {
            progress = operations.size
        } else {
            progress += value
        }
    }

    fun getStatus(): Status = status

    fun getProgress() = progress

    fun getOperations(): List<Operation<I>> = operations

    override fun getName(): String {
        request<String> {
            requestId(id = 2)
            requestName(name = "")
            requestOperations {
                item(element = "")
            }
        }
        return name
    }

    abstract class Status {
        companion object {
            fun empty(): Status = object : Status() {

            }
        }
    }

    @RequestBuilderMarker
    class Builder<I> {
        private var _name = "Simple request"
        private var _id = Random.nextInt()
        private val _operations = mutableListOf<Operation<I>>()

        fun requestName(name: String) {
            _name = name
        }

        fun requestId(id: Int) {
            _id = id
        }

        fun requestOperations(block: MutableList<Operation<I>>.() -> Unit) {
            _operations.addAll(mutableListOf<Operation<I>>().apply(block))
        }

        fun build(): Request<I> = Request(name = _name, id = _id, operations = _operations)
    }

}