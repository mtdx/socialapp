import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { RetweetAccount } from './retweet-account.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RetweetAccountService {

    private resourceUrl = 'api/retweet-accounts';
    private resourceSearchUrl = 'api/_search/retweet-accounts';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(retweetAccount: RetweetAccount): Observable<RetweetAccount> {
        const copy = this.convert(retweetAccount);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(retweetAccount: RetweetAccount): Observable<RetweetAccount> {
        const copy = this.convert(retweetAccount);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<RetweetAccount> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.created = this.dateUtils
            .convertDateTimeFromServer(entity.created);
    }

    private convert(retweetAccount: RetweetAccount): RetweetAccount {
        const copy: RetweetAccount = Object.assign({}, retweetAccount);

        copy.created = this.dateUtils.toDate(retweetAccount.created);
        return copy;
    }
}
