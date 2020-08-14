package com.huawei.codelabs.hiresearch.healthstudy.model;

import androidx.annotation.NonNull;

import com.huawei.hiresearch.common.annotation.metadata.HiResearchMetadata;
import com.huawei.hiresearch.common.annotation.metadata.HiResearchRemoveDuplication;
import com.huawei.hiresearch.common.model.metadata.HiResearchBaseMetadata;
import com.huawei.hiresearch.skin.model.atrial.AtrialFibrillationMeasureResult;
import com.huawei.hiresearch.skin.model.atrial.AtrialFibrillationMeasureResultType;
import com.huawei.hiresearch.skin.model.atrial.AtrialMeasureResult;

import static com.huawei.hiresearch.common.annotation.metadata.HiResearchMetaType.DEFAULT;

@HiResearchMetadata(name = "AtrialFibrillationMeasureResult", version = "1", metaType = DEFAULT)
@HiResearchRemoveDuplication(primaryKey = "userNo", useHealthCode = true)
public class AtrialFibrillationMeasureResultMetadata extends HiResearchBaseMetadata {
    /**
     * 用户编号
     */
    private String userNo;

    /**
     * atrialFibrillationType
     */
    private String result;

    /**
     * 房颤概率
     */
    private String probability;

    /**
     * 房颤风险等级
     */
    private String riskLevel;

    /**
     * constructor
     */
    public AtrialFibrillationMeasureResultMetadata() {
    }

    /**
     * constructor
     *
     * @param userNo
     * @param atrialFibrillationType
     * @param atrialFibrillationProbability
     * @param atrialFibrillationRiskLevel
     */
    public AtrialFibrillationMeasureResultMetadata(String userNo,
                                                   String atrialFibrillationType,
                                                   String atrialFibrillationProbability,
                                                   String atrialFibrillationRiskLevel) {
        this.userNo = userNo;
        this.probability = atrialFibrillationProbability;
        this.riskLevel = atrialFibrillationRiskLevel;
        this.result = atrialFibrillationType;
    }

    /**
     * constructor
     *
     * @param userNo        用户编号
     * @param measureResult 房颤监测结果
     */
    public AtrialFibrillationMeasureResultMetadata(String userNo, AtrialFibrillationMeasureResult measureResult) {
        if (null != measureResult) {
            this.userNo = userNo;
            this.probability = String.format("%.2f", measureResult.getProbability() * 100) + "%";
            this.riskLevel = formatRiskLevl(measureResult.getRiskLevel());
            this.result = formatAtrialFibrillationType(measureResult.getType());
        }
    }

    private String formatRiskLevl(int level) {
        switch (level) {
            case 1:
                return "低风险";
            case 2:
                return "高风险";
            default:
                return "未见异常";
        }
    }


    private String formatAtrialFibrillationType(@AtrialFibrillationMeasureResultType int type) {
        switch (type) {
            case AtrialFibrillationMeasureResultType.SINUS_RHYTHM:
                return "窦性心律";
            case AtrialFibrillationMeasureResultType.SUSPECTED_ATRIAL_FIBRILLATION:
                return "疑似房颤";
            case AtrialFibrillationMeasureResultType.TO_BE_CONFIRMED:
                return "待确认";
            default:
                return "待确认";
        }
    }

    /**
     * constructor
     *
     * @param userNo        用户编号
     * @param measureResult 心房监测结果
     */
    public AtrialFibrillationMeasureResultMetadata(String userNo, @NonNull AtrialMeasureResult measureResult) {
        this(userNo, measureResult.getAtrialFibrillationMeasureResult());
    }

    public String getUserNo() {
        return userNo;
    }

    public String getResult() {
        return result;
    }

    public String getProbability() {
        return probability;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
}
