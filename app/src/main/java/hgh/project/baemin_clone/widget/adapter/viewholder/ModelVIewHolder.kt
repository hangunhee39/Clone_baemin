package hgh.project.baemin_clone.widget.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import hgh.project.baemin_clone.model.Model
import hgh.project.baemin_clone.screen.base.BaseViewModel
import hgh.project.baemin_clone.util.provider.ResourceProvider
import hgh.project.baemin_clone.widget.adapter.listener.AdapterListener

abstract class ModelVIewHolder<M: Model>(
    binding:ViewBinding,
    protected val viewModel: BaseViewModel,
    protected val resourceProvider: ResourceProvider
): RecyclerView.ViewHolder(binding.root) {

    abstract fun reset()

    open fun bindData(model: M){
        reset()
    }

    abstract fun bindViews(model: M, adapterListener: AdapterListener)
}