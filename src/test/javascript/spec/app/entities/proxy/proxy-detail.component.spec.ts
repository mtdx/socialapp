import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { SocialappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProxyDetailComponent } from '../../../../../../main/webapp/app/entities/proxy/proxy-detail.component';
import { ProxyService } from '../../../../../../main/webapp/app/entities/proxy/proxy.service';
import { Proxy } from '../../../../../../main/webapp/app/entities/proxy/proxy.model';

describe('Component Tests', () => {

    describe('Proxy Management Detail Component', () => {
        let comp: ProxyDetailComponent;
        let fixture: ComponentFixture<ProxyDetailComponent>;
        let service: ProxyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SocialappTestModule],
                declarations: [ProxyDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProxyService,
                    EventManager
                ]
            }).overrideTemplate(ProxyDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProxyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProxyService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Proxy(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.proxy).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
