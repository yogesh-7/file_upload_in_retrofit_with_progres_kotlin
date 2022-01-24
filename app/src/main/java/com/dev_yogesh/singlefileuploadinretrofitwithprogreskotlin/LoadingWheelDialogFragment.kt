package com.dev_yogesh.singlefileuploadinretrofitwithprogreskotlin

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.dev_yogesh.singlefileuploadinretrofitwithprogreskotlin.databinding.FragmentLoadingPrecentageBinding


class LoadingWheelDialogFragment : DialogFragment(R.layout.fragment_loading_precentage) {


    private var _binding: FragmentLoadingPrecentageBinding? = null
    // This property is only valid between onCreateView and
        // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingPrecentageBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
                if (activity!!.supportFragmentManager.findFragmentByTag("loading_tag") == null) {
                    activity!!.onBackPressed()
                }
            }
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    fun setProgressValue(progress: Int){
        binding.loadingProgressBar.setProgress(progress)
        binding.tvLoadingProgressBarValue.text = progress.toString().plus(getString(R.string.uploading_scan_value_set))
    }

    companion object {
        fun getInstance(): LoadingWheelDialogFragment {
            val fragment = LoadingWheelDialogFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }

    }
}