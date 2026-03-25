import api from "@/api/ApiConfig.ts";


export interface LoanRequest {
    userId: number,
    loanPeriod: number,
    amount: number
}

export interface LoanResponse {
    amount: number,
    approved: boolean,
    updatedMinimumLoanPeriod: number
}

export async function requestLoan(request: LoanRequest): Promise<LoanResponse> {
    const response = await api.post<LoanResponse>('/loan/apply', request);
    return response.data;
}

export async function healthCheck(): Promise<string> {
    const response = await api.get<string>('/loan/health');
    return response.data;
}