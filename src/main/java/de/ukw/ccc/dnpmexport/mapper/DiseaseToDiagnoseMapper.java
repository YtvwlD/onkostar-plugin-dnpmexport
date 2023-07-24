/*
 * MIT License
 *
 * Copyright (c) 2023 Comprehensive Cancer Center Mainfranken
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.ukw.ccc.dnpmexport.mapper;

import de.itc.onkostar.api.Disease;
import de.itc.onkostar.api.IOnkostarApi;
import de.ukw.ccc.bwhc.dto.Diagnosis;

import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.function.Function;

import static de.ukw.ccc.dnpmexport.mapper.MapperUtils.getPatientId;

public class DiseaseToDiagnoseMapper implements Function<Disease, Optional<Diagnosis>> {

    private final MapperUtils mapperUtils;

    public DiseaseToDiagnoseMapper(final IOnkostarApi onkostarApi) {
        this.mapperUtils = new MapperUtils(onkostarApi);
    }

    @Override
    public Optional<Diagnosis> apply(Disease disease) {
        var formatter = new SimpleDateFormat("yyyy-MM-dd");

        var builder = Diagnosis.builder()
                .withId(mapperUtils.anonymizeId(disease.getId().toString()))
                .withPatient(getPatientId(disease))
                .withRecordedOn(formatter.format(disease.getDiagnosisDate()));

        mapperUtils.getIcd10(disease).ifPresent(builder::withIcd10);
        mapperUtils.getIcdO3T(disease).ifPresent(builder::withIcdO3T);

        return Optional.of(builder.build());
    }

}
